package aoc.dcw;

import aoc.dcw.util.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String SPACE = ".";
    public static final long SPACE_ID = -1;
    String input;

    public Day9(String file) {
        input = Utilities.getString(file);
    }

    public static String FilesToString(Stream<FileBlock> blocks) {
        return BlocksToString(blocks.flatMap(fb -> fb.getBlocks().stream()));
    }

    public static String BlocksToString(Stream<Long> blocks) {
        return blocks.map(i -> i == SPACE_ID ? SPACE : i.toString()).collect(Collectors.joining());
    }

    public FileDisk convertInputToDisk() {
        List<FileBlock> blocks = new ArrayList<>();
        int fileId = 0;
        char[] sizes = input.toCharArray();
        for (int i = 0; i < sizes.length; i++) {
            int size = Character.getNumericValue(sizes[i]);
            long fid = SPACE_ID;
            if (i % 2 == 0) {
                fid = fileId++;
            }
            FileBlock b = new FileBlock(fid, size);
            blocks.add(b);
        }
        return new FileDisk(blocks);
    }

    // Reorder the individual blocks
    public List<Long> reorderBlocks(FileDisk disk) {
        BlockDisk blockDisk = disk.getBlockDisk();
        List<Long> blocks = blockDisk.blocks;
        logger.debug("reordering {}", blockDisk.blocks.size());
        //int lastNonSpaceIndex = lastNonSpace(blocks, blocks.size() - 1);
        int lastNonSpaceIndex = blockDisk.lastNonSpace(blocks.size() - 1);
        logger.debug("lastNonSpace: {}", lastNonSpaceIndex);

        // Start at beginning and any time we encounter a space, swap with the last non-space block
        for (int i = 0; i < blocks.size() && lastNonSpaceIndex >= i; i++) {
            if (blocks.get(i) == SPACE_ID) {
                Collections.swap(blocks, lastNonSpaceIndex, i);
                lastNonSpaceIndex = blockDisk.lastNonSpace(lastNonSpaceIndex);
            }
        }
        return blocks;
    }

    // reorder the files
    public FileDisk reorderFiles(FileDisk disk) {
        List<FileBlock> files = new ArrayList<>(disk.blocks);
        FileDisk reorderedDisk = new FileDisk(files);
        List<FileBlock> nonSpaceFilesReverse = new ArrayList<>(disk.blocks.stream().filter(f -> !f.isSpace()).toList());
        Collections.reverse(nonSpaceFilesReverse);
        do {
            FileBlock file = nonSpaceFilesReverse.get(0);
            int fileIndex = files.indexOf(file);
            int spaceIndex = reorderedDisk.findSpaceOfSize(file.size);
            if (spaceIndex > -1 && spaceIndex < fileIndex) {
                FileBlock space = files.get(spaceIndex);
                // remove some space from this block
                space.take(file.size);
                // move file to space
                files.add(spaceIndex, files.remove(fileIndex));
                // move space to end
                files.add(fileIndex, new FileBlock(SPACE_ID, file.size));
            }
            nonSpaceFilesReverse.remove(0);
        } while (!nonSpaceFilesReverse.isEmpty());
        return new FileDisk(files);
    }

    // Class to manage a list of individual blocks
    public static class BlockDisk {

        final List<Long> blocks;

        public BlockDisk(List<Long> blocks) {
            this.blocks = blocks;
        }

        public int lastNonSpace(int from) {
            for (int i = from; i >= 0; i--) {
                if (blocks.get(i) != SPACE_ID) {
                    return i;
                }
            }
            return -1;
        }
    }

    // Class to manage a list of file or space blocks
    public static class FileDisk {

        public final List<FileBlock> blocks;

        public FileDisk(List<FileBlock> blocks) {
            this.blocks = blocks;
        }

        // Convert the list of files to list of individual blocks
        public BlockDisk getBlockDisk() {
            return new BlockDisk(new ArrayList<>(blocks.stream().flatMap(fb -> fb.getBlocks().stream()).toList()));
        }

        public static long calcCheckSum(Stream<Long> ids) {
            MutableInt blockId = new MutableInt(0);
            return ids.map(id -> {
                long bid = blockId.getAndIncrement();
                return id == SPACE_ID ? 0 : id * bid;
            }).reduce(Long::sum).orElseThrow();
        }

        public String toString() {
            return BlocksToString(blocks.stream().flatMap(fb -> fb.getBlocks().stream()));
        }

        public long calcCheckSum() {
            return calcCheckSum(blocks.stream().flatMap(fb -> Arrays.stream(Utilities.fill(fb.size, fb.fileId)).boxed()));
        }

        public int findSpaceOfSize(int size) {
            for (int i = 0; i < blocks.size(); i++) {
                FileBlock block = blocks.get(i);
                if (block.isSpace() && block.size >= size) return i;
            }
            return -1;
        }
    }

    public static class FileBlock {

        final long fileId;
        int size;

        public FileBlock(long fileId, int size) {
            this.fileId = fileId;
            this.size = size;
        }

        public String toString() {
            return StringUtils.repeat(fileId == SPACE_ID ? SPACE.charAt(0) : ("" + fileId).charAt(0), size);
        }

        public List<Long> getBlocks() {
            return Arrays.stream(Utilities.fill(size, fileId)).boxed().toList();
        }

        public boolean isSpace() {
            return fileId == SPACE_ID;
        }

        public void take(int size) {
            this.size = this.size - size;
        }
    }
}
