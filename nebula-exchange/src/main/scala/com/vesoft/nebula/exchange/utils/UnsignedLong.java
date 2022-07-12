package com.vesoft.nebula.exchange.utils;

import javax.annotation.CheckReturnValue;

public final class UnsignedLong {
    private final long value;
    private UnsignedLong(long value) {
        this.value = value;
    }

    public static UnsignedLong checkNotNull(UnsignedLong reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    private static long flip(long a) {
        return a ^ Long.MIN_VALUE;
    }

    public static int compare(long a, long b) {
        return Long.compare(flip(a), flip(b));
    }

    public static UnsignedLong fromLongBits(long bits) {
        return new UnsignedLong(bits);
    }

    @CheckReturnValue
    public UnsignedLong mod(UnsignedLong val) {
        return fromLongBits(remainder(this.value, ((UnsignedLong)checkNotNull(val)).value));
    }

    public static long remainder(long dividend, long divisor) {
        if (divisor < 0) { // i.e., divisor >= 2^63:
            if (compare(dividend, divisor) < 0) {
                return dividend; // dividend < divisor
            } else {
                return dividend - divisor; // dividend >= divisor
            }
        }

        // Optimization - use signed modulus if dividend < 2^63
        if (dividend >= 0) {
            return dividend % divisor;
        }

        /*
         * Otherwise, approximate the quotient, check, and correct if necessary. Our approximation is
         * guaranteed to be either exact or one less than the correct value. This follows from fact
         * that floor(floor(x)/i) == floor(x/i) for any real x and integer i != 0. The proof is not
         * quite trivial.
         */
        long quotient = ((dividend >>> 1) / divisor) << 1;
        long rem = dividend - quotient * divisor;
        return rem - (compare(rem, divisor) >= 0 ? divisor : 0);
    }

    public int intValue() {
        return (int)this.value;
    }
}