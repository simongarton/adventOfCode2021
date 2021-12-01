package com.simongarton.advent;

import com.simongarton.advent.challenge.Challenge1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(final String[] args) {

        final Main main = new Main();
        main.run();
    }

    private void run() {
        new Challenge1().run();
    }
}
