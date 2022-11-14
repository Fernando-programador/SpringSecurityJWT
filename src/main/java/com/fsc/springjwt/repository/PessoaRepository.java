package com.fsc.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.fsc.springjwt.model.EnunPessoa;
import com.fsc.springjwt.model.Pessoa;

@Repository
@EnableJpaRepositories
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

	Optional<Pessoa> findByNome(EnunPessoa nome);
}
