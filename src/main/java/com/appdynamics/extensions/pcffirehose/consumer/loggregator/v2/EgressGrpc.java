/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.consumer.loggregator.v2;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.0)",
    comments = "Source: egress.proto")
public final class EgressGrpc {

  private EgressGrpc() {}

  public static final String SERVICE_NAME = "loggregator.v2.Egress";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @Deprecated // Use {@link #getReceiverMethod()} instead.
  public static final io.grpc.MethodDescriptor<LoggregatorEgress.EgressRequest,
      LoggregatorEnvelope.Envelope> METHOD_RECEIVER = getReceiverMethod();

  private static volatile io.grpc.MethodDescriptor<LoggregatorEgress.EgressRequest,
      LoggregatorEnvelope.Envelope> getReceiverMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<LoggregatorEgress.EgressRequest,
      LoggregatorEnvelope.Envelope> getReceiverMethod() {
    io.grpc.MethodDescriptor<LoggregatorEgress.EgressRequest, LoggregatorEnvelope.Envelope> getReceiverMethod;
    if ((getReceiverMethod = EgressGrpc.getReceiverMethod) == null) {
      synchronized (EgressGrpc.class) {
        if ((getReceiverMethod = EgressGrpc.getReceiverMethod) == null) {
          EgressGrpc.getReceiverMethod = getReceiverMethod =
              io.grpc.MethodDescriptor.<LoggregatorEgress.EgressRequest, LoggregatorEnvelope.Envelope>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "loggregator.v2.Egress", "Receiver"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  LoggregatorEgress.EgressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  LoggregatorEnvelope.Envelope.getDefaultInstance()))
                  .setSchemaDescriptor(new EgressMethodDescriptorSupplier("Receiver"))
                  .build();
          }
        }
     }
     return getReceiverMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @Deprecated // Use {@link #getBatchedReceiverMethod()} instead.
  public static final io.grpc.MethodDescriptor<LoggregatorEgress.EgressBatchRequest,
      LoggregatorEnvelope.EnvelopeBatch> METHOD_BATCHED_RECEIVER = getBatchedReceiverMethod();

  private static volatile io.grpc.MethodDescriptor<LoggregatorEgress.EgressBatchRequest,
      LoggregatorEnvelope.EnvelopeBatch> getBatchedReceiverMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<LoggregatorEgress.EgressBatchRequest,
      LoggregatorEnvelope.EnvelopeBatch> getBatchedReceiverMethod() {
    io.grpc.MethodDescriptor<LoggregatorEgress.EgressBatchRequest, LoggregatorEnvelope.EnvelopeBatch> getBatchedReceiverMethod;
    if ((getBatchedReceiverMethod = EgressGrpc.getBatchedReceiverMethod) == null) {
      synchronized (EgressGrpc.class) {
        if ((getBatchedReceiverMethod = EgressGrpc.getBatchedReceiverMethod) == null) {
          EgressGrpc.getBatchedReceiverMethod = getBatchedReceiverMethod =
              io.grpc.MethodDescriptor.<LoggregatorEgress.EgressBatchRequest, LoggregatorEnvelope.EnvelopeBatch>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "loggregator.v2.Egress", "BatchedReceiver"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  LoggregatorEgress.EgressBatchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  LoggregatorEnvelope.EnvelopeBatch.getDefaultInstance()))
                  .setSchemaDescriptor(new EgressMethodDescriptorSupplier("BatchedReceiver"))
                  .build();
          }
        }
     }
     return getBatchedReceiverMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EgressStub newStub(io.grpc.Channel channel) {
    return new EgressStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EgressBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new EgressBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EgressFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new EgressFutureStub(channel);
  }

  /**
   */
  public static abstract class EgressImplBase implements io.grpc.BindableService {

    /**
     */
    public void receiver(LoggregatorEgress.EgressRequest request,
        io.grpc.stub.StreamObserver<LoggregatorEnvelope.Envelope> responseObserver) {
      asyncUnimplementedUnaryCall(getReceiverMethod(), responseObserver);
    }

    /**
     */
    public void batchedReceiver(LoggregatorEgress.EgressBatchRequest request,
        io.grpc.stub.StreamObserver<LoggregatorEnvelope.EnvelopeBatch> responseObserver) {
      asyncUnimplementedUnaryCall(getBatchedReceiverMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getReceiverMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                LoggregatorEgress.EgressRequest,
                LoggregatorEnvelope.Envelope>(
                  this, METHODID_RECEIVER)))
          .addMethod(
            getBatchedReceiverMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                LoggregatorEgress.EgressBatchRequest,
                LoggregatorEnvelope.EnvelopeBatch>(
                  this, METHODID_BATCHED_RECEIVER)))
          .build();
    }
  }

  /**
   */
  public static final class EgressStub extends io.grpc.stub.AbstractStub<EgressStub> {
    private EgressStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EgressStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected EgressStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EgressStub(channel, callOptions);
    }

    /**
     */
    public void receiver(LoggregatorEgress.EgressRequest request,
        io.grpc.stub.StreamObserver<LoggregatorEnvelope.Envelope> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getReceiverMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void batchedReceiver(LoggregatorEgress.EgressBatchRequest request,
        io.grpc.stub.StreamObserver<LoggregatorEnvelope.EnvelopeBatch> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getBatchedReceiverMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class EgressBlockingStub extends io.grpc.stub.AbstractStub<EgressBlockingStub> {
    private EgressBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EgressBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected EgressBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EgressBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<LoggregatorEnvelope.Envelope> receiver(
        LoggregatorEgress.EgressRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getReceiverMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<LoggregatorEnvelope.EnvelopeBatch> batchedReceiver(
        LoggregatorEgress.EgressBatchRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getBatchedReceiverMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class EgressFutureStub extends io.grpc.stub.AbstractStub<EgressFutureStub> {
    private EgressFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EgressFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected EgressFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EgressFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_RECEIVER = 0;
  private static final int METHODID_BATCHED_RECEIVER = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EgressImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(EgressImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECEIVER:
          serviceImpl.receiver((LoggregatorEgress.EgressRequest) request,
              (io.grpc.stub.StreamObserver<LoggregatorEnvelope.Envelope>) responseObserver);
          break;
        case METHODID_BATCHED_RECEIVER:
          serviceImpl.batchedReceiver((LoggregatorEgress.EgressBatchRequest) request,
              (io.grpc.stub.StreamObserver<LoggregatorEnvelope.EnvelopeBatch>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class EgressBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EgressBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return LoggregatorEgress.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Egress");
    }
  }

  private static final class EgressFileDescriptorSupplier
      extends EgressBaseDescriptorSupplier {
    EgressFileDescriptorSupplier() {}
  }

  private static final class EgressMethodDescriptorSupplier
      extends EgressBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    EgressMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EgressGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EgressFileDescriptorSupplier())
              .addMethod(getReceiverMethod())
              .addMethod(getBatchedReceiverMethod())
              .build();
        }
      }
    }
    return result;
  }
}
