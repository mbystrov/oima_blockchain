package com.gd.blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    private int numberOfBlocks;
    private int numberOfMiners;
    private int initialComplexity;

    /**
     * Constructor of the Application class
     *
     * @param numberOfBlocks    - number of blocks in a blockchain
     * @param numberOfMiners    - number of miners trying to fill the blockchain
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
        //----With threading v2
        Thread miner1 = new Thread(new Miner(blockchain));
        Thread miner2 = new Thread(new Miner(blockchain));
        Thread miner3 = new Thread(new Miner(blockchain));
        Thread miner4 = new Thread(new Miner(blockchain));

        miner1.start();
        miner2.start();
        miner3.start();
        miner4.start();

        //----With threading v1
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

        //----Without threading
//        do {
//            String hashPrev = blockchain.getHashOneBeforeLast();
//            String prefix =  new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
//            blockchain.addBlock(new Block(blockchain.generateNewId(), hashPrev, prefix, 1));
//            numberOfBlocks--;
//        } while (numberOfBlocks > 0);

        System.out.println(blockchain);
    }
}

/**
 * The class is for the second attempt to implement multithreading to the application
 */
class Miner implements Runnable {
    Blockchain blockchain;
    public static int minerId = 1;

    /**
     * Constructor of the Miner class
     *
     * @param blockchain - a blockchain miner tries to generate block for
     */
    public Miner(Blockchain blockchain) {
        this.blockchain = blockchain;
        minerId++;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String hashPrev = blockchain.getHashOneBeforeLast();
            String prefix = new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
            blockchain.addBlock(new Block(blockchain.generateNewId(), hashPrev, prefix, minerId));
        });
    }
}
