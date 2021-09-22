package com.gd.blockchain;

import java.util.concurrent.ConcurrentHashMap;

public class Blockchain {
    private ConcurrentHashMap<Integer, Block> blockchain = new ConcurrentHashMap<>();
    private int zerosNumber;

    /**
     * Constructor of the Blockchain class
     * @param zerosNumber - number of prefix zeros
     */
    public Blockchain(int zerosNumber) {
        this.zerosNumber = zerosNumber;
    }

    /**
     * Adds block to the blockchain collection. Checks block is valid.
     * @param block - a block to be added to the blockchain
     * @return boolean true if block has been added, otherwise false
     */
    public boolean addBlock(Block block) {
        if (isValidBlock(block)) {
            block.setComplexityMsg(changeComplexity(block));
            blockchain.put(block.getId(), block);
            return true;
        }
        return false;
    }

    /**
     * Changes complexity for the blockchain
     * @param block - block based on which the decision of change complexity is made
     * @return string saying whether the complexity was changed or not
     */
    public String changeComplexity(Block block) {
        String message;
        if (block.getCreationTime() > 60) {
            if (zerosNumber > 0) {
            zerosNumber--;
            }
            message = "N was decreased by 1";
        } else if (block.getCreationTime() < 10 && zerosNumber > 0) {
            zerosNumber++;
            message = String.format("N was increased to %s", block.getZerosPrefix().length() + 1);
        } else {
            message = "N stays the same";
        }
        return message;
    }

    /**
     * Method to receive hashPrev of the last block in the blockchain
     * @return string hashPrev
     */
    public String getHashOneBeforeLast() {
        return blockchain.size() == 0 ? "0" : blockchain.get(blockchain.size()).getHashCurr();
    }

    /**
     * Method to check if a block is valid: its hashCurr starts with zerosPrefix, hashPrev equals to hashCurr of the previous block
     * @param block - block to be validated
     * @return boolean true if block passes validation, otherwise false
     */
    private boolean isValidBlock(Block block) {
        return block.getHashCurr().startsWith(block.getZerosPrefix()) &&
                block.getId() == 1 ? "0".equals(block.getHashPrev()) :
                block.getHashPrev().equals(blockchain.get(block.getId() - 1).getHashCurr());
    }

    /**
     * Method to generate new block id
     * @return int value of block id
     */
    public int generateNewId() {
        if (blockchain.size() == 0) {
            return 1;
        } else {
            return blockchain.size() + 1;
        }
    }

    public int getZerosNumber() {
        return zerosNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (Block block : blockchain.values()) {
            sb.append(block);
        }
        return sb.toString();
    }
}
