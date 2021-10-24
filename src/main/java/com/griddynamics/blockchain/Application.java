package com.griddynamics.blockchain;

import com.griddynamics.blockchain.multithreading.BlockManager;
import com.griddynamics.blockchain.multithreading.Miner;
import com.griddynamics.blockchain.multithreading.MinerManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<Miner> listMiners = IntStream.rangeClosed(1, numberOfMiners)
                .mapToObj(x -> new Miner(x, minerManager, blockManager, blockchain))
                .collect(Collectors.toList());

        listMiners.forEach(executorService::submit);

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