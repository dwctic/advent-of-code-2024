package aoc.dcw;

import aoc.dcw.util.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 {

    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String SPACE = ".";
    public static final long SPACE_ID = -1;

    public final String input;

    public Day9(String file) {
        input = Utilities.getString(file);
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

    // Reorder the individual blocks based on part 1 instructions
    public List<Long> reorderBlocks(FileDisk disk) {
        BlockDisk blockDisk = disk.getBlockDisk();
        List<Long> blocks = blockDisk.blocks;
        logger.debug("reordering {}", blockDisk.blocks.size());
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

    // reorder the files based on part 2 instructions
    public FileDisk reorderFiles(FileDisk disk) {
        List<FileBlock> files = new ArrayList<>(disk.files);
        FileDisk reorderedDisk = new FileDisk(files);
        List<FileBlock> nonSpaceFilesReverse = new ArrayList<>(disk.files.stream().filter(f -> !f.isSpace()).toList());
        Collections.reverse(nonSpaceFilesReverse);
        do {
            FileBlock file = nonSpaceFilesReverse.get(0);
            int fileIndex = files.indexOf(file);
            int spaceIndex = reorderedDisk.findSpaceOfSize(file.size);
            if (spaceIndex > -1 && spaceIndex < fileIndex) {
                FileBlock space = files.get(spaceIndex);
                // remove some space from this block
                space.shrink(file.size);
                // move file to space
                files.add(spaceIndex, files.remove(fileIndex));
                // move space to end
                files.add(fileIndex, new FileBlock(SPACE_ID, file.size));
            }
            nonSpaceFilesReverse.remove(0);
        } while (!nonSpaceFilesReverse.isEmpty());
        return new FileDisk(files);
    }


    public static String FilesToString(Stream<FileBlock> blocks) {
        return BlocksToString(blocks.flatMap(FileBlock::getBlocks));
    }

    public static String BlocksToString(Stream<Long> blocks) {
        return blocks.map(i -> i == SPACE_ID ? SPACE : i.toString()).collect(Collectors.joining());
    }

    // Class to manage a list of individual blocks, regardless of the file id
    public static class BlockDisk {

        final List<Long> blocks; // each value is a file id or -1

        public BlockDisk(List<Long> blocks) {
            this.blocks = blocks;
        }

        // find last block that is not a space
        public int lastNonSpace(int from) {
            for (int i = from; i >= 0; i--) {
                if (blocks.get(i) != SPACE_ID) {
                    return i;
                }
            }
            return -1;
        }
    }

    // Class to manage a list of contiguous file blocks
    public static class FileDisk {

        public final List<FileBlock> files; // each object is file / empty space + length

        public FileDisk(List<FileBlock> files) {
            this.files = files;
        }

        // Convert the disk of files to disk of individual blocks
        public BlockDisk getBlockDisk() {
            return new BlockDisk(new ArrayList<>(files.stream().flatMap(FileBlock::getBlocks).toList()));
        }

        // checksum of the current list of blocks
        public long calcCheckSum() {
            return calcCheckSum(files.stream().flatMap(FileBlock::getBlocks));
        }

        // Find a space file that is at least a certain size
        public int findSpaceOfSize(int size) {
            for (int i = 0; i < files.size(); i++) {
                FileBlock block = files.get(i);
                if (block.isSpace() && block.size >= size) return i;
            }
            return -1;
        }

        public String toString() {
            return BlocksToString(files.stream().flatMap(FileBlock::getBlocks));
        }

        // Checksum of a list of file ids
        public static long calcCheckSum(Stream<Long> fileIds) {
            MutableInt blockId = new MutableInt(0);
            return fileIds.map(id -> {
                long bid = blockId.getAndIncrement();
                return id == SPACE_ID ? 0 : id * bid;
            }).reduce(Long::sum).orElseThrow();
        }
    }

    // class to manage a contiguous length of blocks with a specific file id
    public static class FileBlock {

        final long fileId;
        int size;

        public FileBlock(long fileId, int size) {
            this.fileId = fileId;
            this.size = size;
        }

        public Stream<Long> getBlocks() {
            return Utilities.fillStream(size,fileId);
        }

        public boolean isSpace() {
            return fileId == SPACE_ID;
        }

        public void shrink(int shrinkBy) {
            this.size = this.size - shrinkBy;
        }

        public String toString() {
            return StringUtils.repeat(fileId == SPACE_ID ? SPACE.charAt(0) : ("" + fileId).charAt(0), size);
        }
    }
}
