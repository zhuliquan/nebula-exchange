package com.vesoft.nebula.exchange.utils

import java.nio.{ByteBuffer, ByteOrder}
import org.apache.spark.Partitioner

class NebulaPartitioner(partitions: Int) extends Partitioner {
  require(partitions >= 0, s"Number of partitions ($partitions) cannot be negative.")

  override def numPartitions: Int = partitions

  override def getPartition(key: Any): Int = {
    var part = ByteBuffer
      .wrap(key.asInstanceOf[Array[Byte]], 0, 4)
      .order(ByteOrder.nativeOrder)
      .getInt >> 8
    if (part <= 0) {
      part = part + partitions
    }
    part - 1
  }
}
