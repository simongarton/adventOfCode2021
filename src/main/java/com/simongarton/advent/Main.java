package com.simongarton.advent;

import com.simongarton.advent.challenge.Challenge1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static void main(final String[] args) {

        final Main main = new Main();
        main.run();
    }

    private void run() {
        final long result = new Challenge1().run();
        this.logger.info("Challenge1 : " + result);
    }
}
