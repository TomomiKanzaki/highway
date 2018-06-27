package co.to_kanzaki.highwayfee.api.response

class ErrorResponse(val error: Error){
    class Error(val messages: String)
}
