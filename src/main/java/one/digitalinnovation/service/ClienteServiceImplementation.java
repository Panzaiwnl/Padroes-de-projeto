package one.digitalinnovation.service;

import one.digitalinnovation.model.Cliente;
import one.digitalinnovation.model.ClienteRepository;
import one.digitalinnovation.model.Endereco;
import one.digitalinnovation.model.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ClienteServiceImplementation implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepService viaCepService;


    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() ->{
           Endereco novoEndereco = viaCepService.consultarCep(cep);
           enderecoRepository.save(novoEndereco);
           return novoEndereco;
        });


        clienteRepository.save(cliente);


    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if(clienteBd.isPresent()){
            salvarClienteComCep(cliente);
        }

    };


    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        enderecoRepository.findById(Long.valueOf(cep)).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;

        });
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);

    }
}
