package jp.green_code.shared.dto;

// 配列に再代入すると危険という指摘が上がるが、ここでは起きる可能性が低いので抑止している
@SuppressWarnings("ArrayRecordComponent")
public record MessageKey(String key, Object... args) {
}