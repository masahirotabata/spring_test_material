package com.example.sample.service;

import com.example.sample.SampleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SampleServiceTest {
    @Test
    void test_plus() {
        SampleService sampleService = new SampleService();
        int result = sampleService.plus(3, 4);
        Assertions.assertThat(result).isEqualTo(7);
    }
}
