package com.example.bigsbackendtask.exception

enum class ErrorCode(
        val status: Int,
        val message: String
) {
    NO_CONTENT(204, "데이터가 존재하지 않습니다."),
    FAILED_DATA_FETCH_EXTERNAL_API(500, "외부 API에서 데이터를 가져오는데 실패했습니다."),
    JSON_PARSING_FAIL(500, "JSON 파싱에 실패하였습니다.")
}