package com.gd.blockchain;

import java.util.Date;
import java.util.Random;

public class Block {
    private int id;
    private long timestamp = new Date().getTime();
    private String hashPrev;
    private String hashCurr;
    private long magicNumber;
    private long creationTime;
    private String zerosPrefix;
    private int minerId;
    private String complexityMsg;

    /**
     * Constructor of the Block class
     * @param id - id of a block
     * @param hashPrev - hash of the previous block
     * @param zerosPrefix - prefix for the current hash
     * @param minerId - id of a miner mined the block
     */
    public Block (int id, String hashPrev, String zerosPrefix, int minerId) {
        this.id = id;
        this.hashPrev = hashPrev;
        this.zerosPrefix = zerosPrefix;
        this.minerId = minerId;
        this.hashCurr = generateBlock();
    }

    /**
     * Method to generate hash of the block
     * @return current hash of the block
     */
    public String generateBlock() {
        long startTime = System.currentTimeMillis();
        do {
            magicNumber = new Random().nextInt();
            String dataToHash = id + hashPrev + timestamp + magicNumber;
            hashCurr = StringUtil.applySha256(dataToHash);
        } while (!hashCurr.startsWith(zerosPrefix));
        long endTime = System.currentTimeMillis();
        creationTime = (endTime - startTime) / 1000;
        return hashCurr;
    }

    public int getId() {
        return id;
    }

    public String getHashCurr() {
        return hashCurr;
    }

    public String getHashPrev() {
        return hashPrev;
    }

    public String getZerosPrefix() {
        return zerosPrefix;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setComplexityMsg(String complexityMsg) {
        this.complexityMsg = complexityMsg;
    }

    public String toString() {
        return "Block:\n" +
                "Created by miner # " + minerId + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block: " + "\n" + hashPrev + "\n" +
                "Hash of the block: " + "\n" + hashCurr + "\n" +
                "Block was generating for " + creationTime + " seconds" + "\n" +
                complexityMsg + "\n\n";
    }
}
