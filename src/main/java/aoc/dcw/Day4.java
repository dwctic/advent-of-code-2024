package aoc.dcw;

import aoc.AoC;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class Day4 {

    String[] lines;
    int length = 140;
    String xmas = "XMAS";
    String mas = "MAS";

    public void run() throws IOException {

        lines = IOUtils.toString(AoC.class.getClassLoader().getResourceAsStream("day4.txt"), Charset.defaultCharset()).split("\n");
        int foundXmas = 0;
        int foundMas = 0;

        for(int y=0;y<length;y++){
            for(int x=0;x<length;x++){

                // Search in all directions starting from X
                for (int yd = -1; yd <= 1; yd++) {
                    for (int xd = -1; xd <= 1; xd++) {
                        if(searchXmas(x,y,0,xd,yd)) foundXmas ++;
                    }
                }

                // Look for diagonals only, starting from 'A' index and going out
                int startIndex = 1;
                if( (searchMas(x,y,startIndex,1,1,1) && searchMas(x,y,startIndex,-1,-1,-1) ) || searchMas(x,y,startIndex,1,1,-1) && searchMas(x,y,startIndex,-1,-1,1) ) {
                    // Found SE diagonal
                    if((searchMas(x,y,startIndex,-1,1,1) && searchMas(x,y,startIndex,1,-1,-1) || searchMas(x,y,startIndex,-1,1,-1) && searchMas(x,y,startIndex,1,-1,1)  )) {
                        // Found SW diagonal
                        foundMas++;
                    }
                }
            }
        }

        System.out.println("foundXmas: "+foundXmas);
        System.out.println("foundMas: "+foundMas);
    }

    // Recursive search in a specific direction
    boolean searchXmas(int x, int y, int charIndex, int xDir, int yDir) {
        if(charIndex == xmas.length()) return true;
        else if (y > -1 && y < length && x > -1 && x < length && lines[y].charAt(x) == xmas.charAt(charIndex)) {
            return searchXmas(x + xDir, y + yDir, charIndex + 1, xDir, yDir);
        }
        else return false;
    }

    // Recursive search in a specific x,y, and character index direction
    boolean searchMas(int x, int y, int charIndex,int xDir, int yDir,  int charDir) {
        if(charIndex == mas.length() || charIndex == -1) return true;
        else if (y > -1 && y < length && x > -1 && x < length && lines[y].charAt(x) == mas.charAt(charIndex)) {
            return searchMas(x + xDir, y + yDir, charIndex + charDir, xDir, yDir, charDir);
        }
        else return false;
    }
}
