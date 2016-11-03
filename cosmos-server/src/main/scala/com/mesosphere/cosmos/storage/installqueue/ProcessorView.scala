package com.mesosphere.cosmos.storage.installqueue

import com.mesosphere.cosmos.rpc
import com.mesosphere.cosmos.rpc.v1.model.ErrorResponse
import com.twitter.util.Future

/** This  will be used by the processor to observe the
  * state of the install queue
  */
trait ProcessorView {

  /** Returns the next operation in the queue. The order of the
    * elements will be determined by publish time. Earlier published
    * packages will be at the front of the queue.
    *
    * @return the next pending operation
    */
  def next(): Future[Option[PendingOperation]]

  /** Signals to the install queue that the operation on the package
    * failed. This will "move" the package to the error pile.
    *
    * @param packageCoordinate The package coordinate of the
    *                          package on which the operation failed
    * @param operation         The operation that failed
    * @param failure           The failure that occurred
    * @return a future that will complete when state changes has been completed
    */
  def failure(
    packageCoordinate: rpc.v1.model.PackageCoordinate,
    operation: Operation,
    failure: ErrorResponse
  ): Future[Unit]

  /** Signals to the install queue that the operation on the package has
    * been successful. This will delete the node from the install queue.
    *
    * @param packageCoordinate The package coordinate of the package whose
    *                          operation has succeeded.
    * @return a future that will complete when state changes have been completed
    */
  def success(packageCoordinate: rpc.v1.model.PackageCoordinate): Future[Unit]
}