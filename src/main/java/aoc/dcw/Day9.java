package aoc.dcw;

import aoc.dcw.util.Utilities;
import org.apache.commons.lang3.StringUtils;
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
    public static final char SPACE = '.';
    public static final String SPACE_S = ""+SPACE;
    public static final int SPACE_ID = -1;
    String input;

    public Day9(String file) {

        input = Utilities.getString(file);

        //map = new CharacterMap(file);
    }

    public Disk convertToBlocks() {
        //StringBuilder sb = new StringBuilder();
        List<FileBlock> blocks = new ArrayList<>();
        int fileId = 0;
        char[] sizes = input.toCharArray();
        logger.debug("input: {}", input);
        for (int i = 0; i < sizes.length; i++) {
            int size = Character.getNumericValue(sizes[i]);
            //char blockChar;
            int fid = SPACE_ID;
            if (i % 2 != 0) {
                //blockChar = SPACE;
            }
            else {
                //blockChar = ("" + fileId).charAt(0);
                fid = fileId++;
            }
            FileBlock b = new FileBlock(fid,size);
            logger.trace("size: {} block: {}", size, fileId);
            //sb.append(StringUtils.repeat(blockChar, size));
            blocks.add(b);
        }
        //logger.debug("ids: {}", sb);
        return new Disk(blocks);
    }
    public List<Integer> reorderBlocks(Disk blocks){
        List<Integer> allBlocks = new ArrayList<>(blocks.blocks.stream().flatMap(fb -> fb.getBlocks().stream()).toList());
        logger.debug("reorder {}",allBlocks.size());
        int lastNonSpaceIndex = lastNonSpace(allBlocks,allBlocks.size()-1);
        logger.debug("lastNonSpace: {}",lastNonSpaceIndex);
        for(int i=0;i<allBlocks.size() && lastNonSpaceIndex >= i;i++){
            Integer blockId = allBlocks.get(i);
            logger.debug("{}: {}",i,blockId);
            if (blockId == SPACE_ID) {
                // move to end
                int lastNonSpaceFile = allBlocks.get(lastNonSpaceIndex);
                logger.debug("swap space {} to {}",i,lastNonSpaceIndex);
                allBlocks.set(lastNonSpaceIndex,SPACE_ID);
                allBlocks.set(i,lastNonSpaceFile);
                lastNonSpaceIndex = lastNonSpace(allBlocks,lastNonSpaceIndex);
            }
            //String currentState = BlocksToString(allBlocks.stream());
            //logger.debug("currentState: {}",currentState);

        }
        return allBlocks;
    }

    public Disk reorderFiles(Disk disk){

        List<FileBlock> allBlocks = new ArrayList<>(disk.blocks);
        List<FileBlock> reverseFiles = new ArrayList<>(disk.blocks.stream().filter(f -> f.fileId != SPACE_ID).toList());
        Collections.reverse(reverseFiles);
        //int fileIndex = 0;
        boolean done = false;
        do {
            logger.debug("try to move index {}",0);
            FileBlock file = reverseFiles.get(0);
            logger.debug("try to move file {}",file);
            int spaceIndex = findSpaceOfSize(allBlocks,file.size);
            int fileIndex = allBlocks.indexOf(file);
            if(spaceIndex > -1 && spaceIndex < fileIndex) {

                logger.debug("found space at file index {}", spaceIndex);
                FileBlock space = allBlocks.get(spaceIndex);
                space.take(file.size);
                FileBlock newSpace = new FileBlock(SPACE_ID,file.size);
                logger.debug("space now has {}", space.size);
                // insert file in front
                allBlocks.remove(file);

                allBlocks.add(spaceIndex,file);
                allBlocks.add(fileIndex,newSpace);

                reverseFiles.remove(0);
                //if(space.size > file.size) {
                    //space.take(file.size);

                //}
                //fileIndex = 0;
                //logger.debug("current state: {}",FilesToString(allBlocks.stream()));
            }
            else {
                reverseFiles.remove(0);
                //fileIndex++;
            }

        }
        while(!reverseFiles.isEmpty());

        return new Disk(allBlocks);

    }



    public int findSpaceOfSize(List<FileBlock> blocks, int size){
        for(int i=0;i<blocks.size();i++){
            FileBlock block = blocks.get(i);
            if(block.isSpace() && block.size >= size) {
                return i;
                /*if(block.size > size){
                    logger.debug("split block {} of size {} into {}",i,block.size,size);
                    block.split(size);
                }*/
            }
        }
        return -1;
    }

    public long calcCheckSum(Disk disk) {
        long sum = 0;
        long block = 0;
        for(FileBlock fb : disk.blocks) {
            for(int i=0;i<fb.size;i++) {
                if(!fb.isSpace()) sum += (long) fb.fileId * block;
                block++;
            }
        }
        /*for(int i=0;i<ids.size();i++){
            int fileId = ids.get(i);
            if(fileId == SPACE_ID) break;
            sum += (long) fileId *i;
        }*/
        return sum;
    }

    public long calcCheckSum(List<Integer> ids) {
        long sum = 0;
        for(int i=0;i<ids.size();i++){
            int fileId = ids.get(i);
            if(fileId == SPACE_ID) break;
            sum += (long) fileId *i;
        }
        return sum;
    }

    public static int lastNonSpace(List<Integer> blocks, int from){
        for(int i=from;i>=0;i--){
            logger.debug("checking: {}",i);
            if(blocks.get(i) != SPACE_ID) {
                return i;
            }
        }
        return -1;
    }
    public static class FileBlock {
        final int fileId;
        int size;
        public FileBlock(int fileId, int size) {
            this.fileId = fileId;
            this.size = size;
        }
        public String toString() {
            return StringUtils.repeat(fileId == SPACE_ID ? SPACE : (""+fileId).charAt(0), size);
        }
        public List<Integer> getBlocks() {
            int[] blocks = new int[size];
            Arrays.fill(blocks,fileId);
            return Arrays.stream(blocks).boxed().toList();
        }
        public boolean isSpace(){
            return fileId == SPACE_ID;
        }
        public void take(int size){
            this.size = this.size - size;
        }
    }

   public static String FilesToString(Stream<FileBlock> blocks) {
       return BlocksToString(blocks.flatMap(fb -> fb.getBlocks().stream()));
   }
    public static String BlocksToString(Stream<Integer> blocks) {
        return blocks.map(i -> i == SPACE_ID ? SPACE_S : i.toString()).collect(Collectors.joining());
    }
    public static class Disk {
        public final List<FileBlock> blocks;

        public Disk(List<FileBlock> blocks) {
            this.blocks = blocks;
        }
        public String toString() {
            //return blocks.stream().flatMap(fb -> fb.getBlocks().stream()).map(i -> i == SPACE_ID ? SPACE_S : i.toString()).collect(Collectors.joining());
            return BlocksToString(blocks.stream().flatMap(fb -> fb.getBlocks().stream()));
        }
    }

}
