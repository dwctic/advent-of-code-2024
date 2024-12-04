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

                //Any direction
                for(int yd=-1;yd<=1;yd++){
                    for(int xd=-1;xd<=1;xd++){
                        if(searchXmas(x,y,xd,yd,0)) foundXmas ++;
                    }
                }

                // Look for diagonals only
                if( (searchMas(x,y,1,1,1,1) && searchMas(x,y,1,-1,-1,-1) ) || searchMas(x,y,1,1,1,-1) && searchMas(x,y,1,-1,-1,1) ) {
                    // SE diagonal
                    if((searchMas(x,y,1,-1,1,1) && searchMas(x,y,1,1,-1,-1) || searchMas(x,y,1,-1,1,-1) && searchMas(x,y,1,1,-1,1)  )) {
                        // SW diagonal
                        foundMas++;
                    }
                }
            }
        }

        System.out.println("foundXmas: "+foundXmas);
        System.out.println("foundMas: "+foundMas);
    }

    boolean searchXmas(int x, int y, int xd, int yd, int i) {
        if(i == xmas.length()) return true;
        else if(y > -1 && y< 140 && x >-1 && x <140 && lines[y].charAt(x) == xmas.charAt(i)) {
            return searchXmas(x + xd, y +yd,xd,yd,i+1);
        }
        else return false;
    }

    boolean searchMas(int x, int y, int i,int xd, int yd,  int id) {
        if(i == mas.length() || i == -1) return true;
        else if (y > -1 && y < 140 && x > -1 && x < 140 && lines[y].charAt(x) == mas.charAt(i)) {
            return searchMas(x + xd, y + yd, i + id, xd, yd, id);
        }
        else return false;
    }
}
