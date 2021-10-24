package com.griddynamics.blockchain.multithreading;

public class NewBlockInfo {
    private int blockId;
    private String hashPrev;
    private String prefix;

    public NewBlockInfo(int blockId, String hashPrev, String prefix) {
        this.blockId = blockId;
        this.hashPrev = hashPrev;
        this.prefix = prefix;
    }

    public int getBlockId() {
        return blockId;
    }

    public String getHashPrev() {
        return hashPrev;
    }

    public String getPrefix() {
        return prefix;
    }
}
