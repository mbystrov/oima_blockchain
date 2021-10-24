package com.griddynamics.blockchain;

import com.griddynamics.blockchain.multithreading.BlockchainManager;
import com.griddynamics.blockchain.multithreading.Miner;
import com.griddynamics.blockchain.multithreading.MinerManager;
import com.griddynamics.blockchain.multithreading.TransactionManager;

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

        TransactionManager transactionManager = new TransactionManager();
        BlockchainManager blockchainManager = new BlockchainManager(numberOfBlocks, blockchain);
        MinerManager minerManager = new MinerManager(transactionManager, blockchainManager);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfMiners);
        List<Miner> listMiners = IntStream.rangeClosed(1, numberOfMiners)
                .mapToObj(minerId -> new Miner(minerId, minerManager))
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