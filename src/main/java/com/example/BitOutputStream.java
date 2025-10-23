package com.example;

import java.io.IOException;
import java.io.OutputStream;
public class BitOutputStream implements AutoCloseable{
    //class to write bytes on compressed file
    private OutputStream out;//file to write on
    private int currByte = 0;
    private int currNumBytes = 0;

    public BitOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writeBit(int bit) throws IOException {
        currByte = (currByte << 1) | bit;
        currNumBytes++;
        if (currNumBytes == 8) {
            out.write(currByte);
            //new Byte
            currByte = 0;
            currNumBytes = 0;
        }
    }

    public void close() throws IOException {
        if (currNumBytes > 0) {
            currByte = currByte << (8 - currNumBytes);//insert rest bits to complete byte
            out.write(currByte);
        }
        out.close();
    }
}