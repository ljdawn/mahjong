package websiteschema.mpsegment.hmm

/**
 * reference: http://www.cs.umb.edu/~srevilak/viterbi/
 */

trait Viterbi {

  def getConditionProb(statePath: Array[Int], state: Int): Double
  def getProb(state: Int, observe: Int): Double
  def getPi(state: Int): Double

  def getStatesBy(observe: Int): java.util.Collection[Int]

  def calculateResult(listObserve: Seq[Int]): ViterbiResult = {
    val length = listObserve.size
    val ret = new ViterbiResult(length)

    val n = 2
    val o1 = listObserve(0)

    val relatedStates = getStatesBy(o1)

    val relatedStatesCount = relatedStates.size
    initResultInPosition(ret, 0, relatedStatesCount)

    var index = 0
    val iterRlatedStates = relatedStates.iterator()
    while (iterRlatedStates.hasNext) {
      val s = iterRlatedStates.next()
      ret.states(0)(index) = s
      ret.delta(0)(index) = Math.log(getPi(s)) + Math.log(getProb(s, o1))
      ret.psai(0)(index) = 0
      index += 1
    }

    for (p <- 1 until listObserve.size) {
      val oi = listObserve(p)

      val stateSet = getStatesBy(oi)
      initResultInPosition(ret, p, stateSet.size)
      var i = 0
      val iterStateSet = stateSet.iterator()
      while (iterStateSet.hasNext) {
        val state = iterStateSet.next()
        ret.states(p)(i) = state
        var maxDelta = Double.NegativeInfinity
        var maxPsai = Double.NegativeInfinity
        var ls = 0
        var j = 0
        while (j < ret.states(p - 1).length) {
          val statePath = ret.getStatePath(p - 1, n - 1, j)
          val b = Math.log(getProb(state, oi))
          val Aij = Math.log(getConditionProb(statePath, state))
          val psai_j = ret.delta(p - 1)(j) + Aij
          val delta_j = psai_j + b
          if (delta_j > maxDelta) {
            maxDelta = delta_j
          }

          if (psai_j > maxPsai) {
            maxPsai = psai_j
            ls = j
          }
          j += 1
        }

        ret.delta(p)(i) = maxDelta
        ret.psai(p)(i) = ls

        i += 1
      }
    }

    ret
  }

  private def initResultInPosition(ret: ViterbiResult, position: Int, relatedStatesCount: Int) {
    ret.states(position) = new Array[Int](relatedStatesCount)
    ret.delta(position) = new Array[Double](relatedStatesCount)
    ret.psai(position) = new Array[Int](relatedStatesCount)
  }
}

class HmmViterbi extends Viterbi {

  var stateBank = new NodeRepository()
  var observeBank = new NodeRepository()
  var tran: ITransition = Transition()
  var pi = Pi()
  var e = Emission()

  def getE(): Emission = {
    return e
  }

  def setE(e: Emission) {
    this.e = e
  }

  def getObserveBank(): NodeRepository = {
    return observeBank
  }

  def setObserveBank(observeBank: NodeRepository) {
    this.observeBank = observeBank
  }

  def getPi(): Pi = {
    return pi
  }

  def setPi(pi: Pi) {
    this.pi = pi
  }

  def getStateBank(): NodeRepository = {
    return stateBank
  }

  def setStateBank(stateBank: NodeRepository) {
    this.stateBank = stateBank
  }

  def setTran(tran: ITransition) {
    this.tran = tran
  }

  override def getConditionProb(statePath: Array[Int], state: Int) = tran.getConditionProb(statePath, state)

  override def getProb(state: Int, observe: Int) = e.getProb(state, observe)

  override def getPi(state: Int) = pi.getPi(state)

  def getObserveIndex(observe: String): Int = {
    val node = observeBank.get(observe)
    if (node != null) {
      node.getIndex()
    } else {
      val newNode = Node(observe)
      observeBank.add(newNode)
      newNode.getIndex()
    }
  }

  override def getStatesBy(observe: Int): java.util.Collection[Int] = {
    val states = e.getStatesBy(observe)
    if (null == states || states.isEmpty) {
      throw new ObserveListException("UNKNOWN observe object " + observe + ".")
    }
    states
  }

  def calculateWithLog(listObserve: Seq[String]): Seq[Node] = {
    if (listObserve.isEmpty) {
      List[Node]()
    } else {
      val ret = calculateResult(listObserve.map(getObserveIndex(_)))

      val statePath = ret.getStatePath(listObserve.size - 1, listObserve.length, ret.getMaxProbPathId)
      statePath.map(getStateBank.get(_))
    }
  }
}
