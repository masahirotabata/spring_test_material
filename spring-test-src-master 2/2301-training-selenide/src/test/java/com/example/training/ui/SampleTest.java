package com.example.training.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

class SampleTest {
    @Test
    void test() {
        Selenide.open("https://www.google.com");
        Selenide.$("*[name=q]").should(Condition.focused);
    }
}

