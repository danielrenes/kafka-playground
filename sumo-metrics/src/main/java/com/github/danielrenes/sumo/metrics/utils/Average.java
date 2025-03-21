package com.github.danielrenes.sumo.metrics.utils;

public record Average(double sum, long count) {
    public double get() {
        return sum / count;
    }
}
