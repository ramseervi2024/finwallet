package com.rps.finwallet.xlearning;

import com.rps.finwallet.codinginterview.InterviewMasterRunner;

public class InterviewPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "Coding Interview Crash Course (Arrays, Strings, Streams, Concurrency & Patterns)";
    }

    @Override
    public void run() {
        InterviewMasterRunner.runAllInterviewQuestions();
    }
}
