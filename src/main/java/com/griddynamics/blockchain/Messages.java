package com.griddynamics.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Messages {
    private static final List<String> messages = new ArrayList<>() {{
        add("First message");
        add("Second message");
        add("Third message");
        add("Fourth message");
        add("Fifth message");
        add("Sixth message");
        add("Seventh message");
        add("Eighth message");
    }};
    private static final Random random = new Random();

    public static String getRandomMessage() {
        return messages.get(random.nextInt(messages.size()));
    }
}
