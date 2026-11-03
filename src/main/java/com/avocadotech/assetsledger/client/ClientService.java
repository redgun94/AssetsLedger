package com.avocadotech.assetsledger.client;

import com.avocadotech.assetsledger.client.dto.ClientRequestDto;
import com.avocadotech.assetsledger.client.dto.ClientResponseDto;
import com.avocadotech.assetsledger.client.exception.ClientEmailAlreadyExistsException;
import com.avocadotech.assetsledger.client.exception.ClientNotFoundException;
import com.avocadotech.assetsledger.loanofficer.LoanOfficer;
import com.avocadotech.assetsledger.loanofficer.LoanOfficerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final LoanOfficerService loanOfficerService;

    public ClientService(ClientRepository clientRepository, LoanOfficerService loanOfficerService) {
        this.clientRepository = clientRepository;
        this.loanOfficerService = loanOfficerService;
    }

    @Transactional
    public ClientResponseDto createClient(UUID userId, ClientRequestDto clientRequestDto){

        LoanOfficer loanOfficerOwner = loanOfficerService.findProfileOf(userId);
        String email = clientRequestDto.email().trim().toLowerCase();

        if(clientRepository.existsByLoanOfficerIdAndEmail(loanOfficerOwner.getId(),email)){
            throw  new ClientEmailAlreadyExistsException(email);
        }

        Client client = new Client();
        client.setFirstName(clientRequestDto.firstName());
        client.setLastName(clientRequestDto.lastName());
        client.setEmail(email);
        client.setPhone(clientRequestDto.phone());
        client.setServiceStatus(Client.ServiceStatus.NO_ACTION);
        client.setLoanOfficer(loanOfficerOwner);
        Client saved =  clientRepository.save(client);

        return returnClientResponseDto(saved);
    }

    public ClientResponseDto getById(UUID clientId ,UUID userId) {
        LoanOfficer loanOfficer = loanOfficerService.findProfileOf(userId);
        return returnClientResponseDto(clientRepository.findByIdAndLoanOfficerId(clientId, loanOfficer.getId())
                .orElseThrow(()->new ClientNotFoundException(clientId)));
    }

    public List<ClientResponseDto> getMyClients(UUID id) {
        LoanOfficer loanOfficer = loanOfficerService.findProfileOf(id);
        return clientRepository.findAllByLoanOfficerId(loanOfficer.getId()).stream().map(this::returnClientResponseDto).toList();
    }

    @Transactional
    public ClientResponseDto updateClient(UUID clientId, UUID userId, ClientRequestDto clientRequestDto) {
        LoanOfficer loanOfficer = loanOfficerService.findProfileOf(userId);
        Client client = clientRepository.findByIdAndLoanOfficerId(clientId, loanOfficer.getId())
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        String email = clientRequestDto.email().trim().toLowerCase();
        if (!email.equals(client.getEmail())
                && clientRepository.existsByLoanOfficerIdAndEmail(loanOfficer.getId(), email)) {
            throw new ClientEmailAlreadyExistsException(email);
        }

        client.setFirstName(clientRequestDto.firstName());
        client.setLastName(clientRequestDto.lastName());
        client.setEmail(email);
        client.setPhone(clientRequestDto.phone());
        Client saved = clientRepository.save(client);

        return returnClientResponseDto(saved);
    }

    private ClientResponseDto returnClientResponseDto(Client client) {
        return new ClientResponseDto(
                client.getId(),
                client.getEmail(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhone(),
                client.getServiceStatus(),
                client.getCreatedAt()
        );
    }
}
