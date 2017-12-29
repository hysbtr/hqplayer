package com.hysbtr.hqplayer.options;

public class DisposableOption<T> {
    private T value;
    private T defaultValue;

    public DisposableOption(T value, T defaultValue) {
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public T getAndReset() {
        T result = value;
        value = defaultValue;
        return result;
    }

    public void set(T value) {
        this.value = value;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

}