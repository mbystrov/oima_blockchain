package com.gd.blockchain.multithreading;

import com.gd.blockchain.Block;
import com.gd.blockchain.Blockchain;
import com.gd.blockchain.crypt.KeyPairCreator;
import com.gd.blockchain.crypt.Message;
import com.gd.blockchain.utils.Tuple;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BlockManager {
    private int blockNumber;
    private final int maxBlockchainSize;
    private boolean continues = true;
    private final MinerManager minerManager;
    private final Blockchain blockchain;
    private final List<String> messages = new ArrayList<>();
    private ConcurrentHashMap<Integer, Tuple<PublicKey, Integer>> minerKey = new ConcurrentHashMap<>();

    public BlockManager(MinerManager minerManager, int maxSize, Blockchain blockchain) {
        this.maxBlockchainSize = maxSize;
        this.minerManager = minerManager;
        this.blockchain = blockchain;
    }

    public void addBlock(Block block) {
        block.setMessage(messages);
        blockchain.addBlock(block);
        messages.clear();
        blockNumber++;
        if (blockNumber >= maxBlockchainSize) {
            continues = false;
        }
        minerManager.notifyAllMiners();
    }

    public boolean isContinues() {
        return continues;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void sendMessage(Message message) {
        int identifier = message.getIdentifier();
        byte[] signedMsg = message.getSignedMsg();
        String msgText = message.getText();
        int minerId = message.getMinerId();
        Tuple<PublicKey, Integer> minerIdTuple = minerKey.get(minerId);
        if (minerIdTuple.l == identifier) {
            try {
                if (KeyPairCreator.verifySignature(msgText.getBytes(), signedMsg, minerIdTuple.k)) {
                    messages.add(msgText);
                    minerKey.put(minerId, new Tuple<>(minerIdTuple.k, minerIdTuple.l + 1));
                } else {
                    System.out.println(minerId + " Signature is wrong");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(minerId + " sent msg with expired identifier");
        }
    }

    public void sendPublicKey(int minerId, PublicKey publicKey) {
        System.out.println(minerId + " miner id");
        minerKey.put(minerId, new Tuple<>(publicKey, 0));
    }
}
