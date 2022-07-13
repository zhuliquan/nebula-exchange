package com.vesoft.nebula.exchange.utils;

object UnsignedLong {

    def flip(a: Long): Long = {
        a ^ Long.MinValue
    }

    def compare(a: Long, b: Long): Long = {
        flip(a).compare(flip(b))
    }

    def mod(dividend: Long, divisor: Long): Long = {
        if (divisor < 0) { // i.e., divisor >= 2^63:
            if (compare(dividend, divisor) < 0) {
                dividend // dividend < divisor
            } else {
                dividend - divisor // dividend >= divisor
            }
        } else if (dividend >= 0) {
            // Optimization - use signed modulus if dividend < 2^63
            dividend % divisor
        } else {
            /*
         * Otherwise, approximate the quotient, check, and correct if necessary. Our approximation is
         * guaranteed to be either exact or one less than the correct value. This follows from fact
         * that floor(floor(x)/i) == floor(x/i) for any real x and integer i != 0. The proof is not
         * quite trivial.
         */
            var quotient: Long = ((dividend >>> 1) / divisor) << 1
            var rem: Long = dividend - quotient * divisor
            var sub: Long = if (compare(rem, divisor) >= 0) {
                divisor
            } else {
                0
            }
            rem - sub
        }
    }
}