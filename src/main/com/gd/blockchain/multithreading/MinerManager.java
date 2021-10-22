package com.gd.blockchain.multithreading;

import java.util.ArrayList;
import java.util.List;

public class MinerManager {
    private final List<Miner> list = new ArrayList<>();

    public void addMiner(Miner miner) {
        list.add(miner);
    }

    public void notifyAllMiners() {
        list.forEach(Miner::update);
    }

    public List<Miner> getMiners() {
        return list;
    }
}
