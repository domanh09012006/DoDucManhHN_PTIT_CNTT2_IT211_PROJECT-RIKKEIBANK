package org.example.rikkeibank.service;

import org.example.rikkeibank.dto.request.TransferRequest;
import org.example.rikkeibank.dto.response.TransferResponse;

public interface TransferService {

    TransferResponse transfer(TransferRequest request);
}