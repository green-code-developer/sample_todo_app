package jp.green_code.shared.dto;

public record EnumLabel<T extends Enum<T>>(T value, String label) {
}