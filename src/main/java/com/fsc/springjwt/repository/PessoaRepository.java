package com.fsc.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsc.springjwt.model.EnunPessoa;
import com.fsc.springjwt.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

	Optional<Pessoa> findByName(EnunPessoa name);
}
