package com.example.upraxisexam.data.util

import java.io.IOException

class NullAPIResponseBodyException(message: String) : IOException(message)
class UnsuccessfulAPIResponseException(message: String) : IOException(message)
class NoActiveNetworkException(message: String) : IOException(message)