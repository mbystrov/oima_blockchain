package com.gd.blockchain;

import com.gd.blockchain.multithreading.BlockManager;
import com.gd.blockchain.multithreading.Miner;
import com.gd.blockchain.multithreading.MinerManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Application {
    private final int numberOfBlocks;
    private final int numberOfMiners;
    private final int initialComplexity;

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
     */
    public void startApp() {
        Blockchain blockchain = new Blockchain(initialComplexity);

        MinerManager minerManager = new MinerManager();
        BlockManager blockManager = new BlockManager(minerManager, numberOfBlocks, blockchain);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfMiners);
        IntStream.rangeClosed(1, numberOfMiners)
                .mapToObj(x -> new Miner(x, minerManager, blockManager, blockchain))
                .forEach(executorService::submit);

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.MINUTES))
            System.err.println("Threads didn't finish in 10 minutes");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(blockchain);
    }
}