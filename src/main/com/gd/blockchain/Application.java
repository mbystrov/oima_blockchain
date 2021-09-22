package com.gd.blockchain;

public class Application {
    private int numberOfBlocks;
    private int numberOfMiners;
    private int initialComplexity;

    /**
     * Constructor of the Application class
     * @param numberOfBlocks - number of blocks in a blockchain
     * @param numberOfMiners - number of miners trying to fill the blockchain
     * @param initialComplexity - initial number of prefix zeros
     */
    public Application(int numberOfBlocks, int numberOfMiners, int initialComplexity) {
        this.numberOfBlocks = numberOfBlocks;
        this.numberOfMiners = numberOfMiners;
        this.initialComplexity = initialComplexity;
    }

    /**
     * Method to start the application
     * ToDo add multithreading
     */
    public void startApp() {
        Blockchain blockchain = new Blockchain(initialComplexity);
//        ExecutorService executor = Executors.newFixedThreadPool(numberOfMiners);
//
//        for (int i = 1; i <= numberOfMiners; i++) {
//            executor.submit(() -> {
//                String hashPrev = blockchain.getHashOneBeforeLast();
//                String prefix =  new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
//                blockchain.addBlock(new Block(blockchain.generateNewId(), hashPrev, prefix, 1));
//                numberOfBlocks--;
//            });
//        }

        do {
            String hashPrev = blockchain.getHashOneBeforeLast();
            String prefix =  new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
            blockchain.addBlock(new Block(blockchain.generateNewId(), hashPrev, prefix, 1));
            numberOfBlocks--;
        } while (numberOfBlocks > 0);

        System.out.println(blockchain);
    }
}
