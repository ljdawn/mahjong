package me.yingrui.segment.neural

import me.yingrui.segment.math.Matrix

object SoftmaxLayer {

  class BPSoftmaxLayer(var weight: Matrix, var bias: Matrix) extends BPLayer {

    def layer = new SingleLayer(weight, Softmax(), bias, false)

    def size = layer.size

    def calculateDelta(actual: Matrix, error: Matrix): Matrix = error

  }

  def apply(weight: Matrix): BPLayer = new BPSoftmaxLayer(weight, Matrix(1, weight.col))
}
