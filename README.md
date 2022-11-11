# spring-security-jwt
neste projeto vou usar  spring boot, spring security com JKWT Aythentication &amp;amp authorization e refresh de token

Usando banco de Dados MYSQL

dependencias -> spring-boo-started-data-jpa
-> Spring-boot-starter-security
-> spring-boot-starter-web
->spring-boot-starter-validation
->mysql-connector-java
->jjwt <version>0.9.1</version>
-> spring-boot-starter-test
-> spring-security-test


comecei criando os model, repository


nome das tabelas 
-> usuarios
-> pessoas
-> usuario_pessoas = JOIN
-> refreshtoken

seuqencias das classe a serem implementadas

  PACOTE  -> advice  =  CLASS ErrorMessage.java
					 =	CLASS TokenControllerAdvice.java -> @RestControllerAdvice -> Metodo(HandleTokenfreshException = @ExceptionHandler @ResponseStatus)
																										
  
  PACOTE -> controllers = CLASS AuthController.java -> @CrossOrigin 					-> metodo authenticateUser  = @PostMapping("/signin")
											  -> @RestController				-> metodo registerUser		= @PostMapping("/signup")
											  -> @RequetMapping("/api/auth")    -> metodo refreshtoken		= @PostMapping("/refreshtoken")
																				-> metodo logoutUser		= @PostMapping("/singout")
																				
						= CLASS TestController.java -> @CrossOrigin 					-> metodo allAcess			=@GetMapping("/all") @PreAuhorize
											  -> @RestController				-> metodo userAcess			=@GetMapping("/user") @PreAuhorize 
											  -> @RequetMapping("/api/test")	-> metodo moderatorAcess	=@GetMapping("/mod") @PreAuhorize
																				-> metodo adminAcess		=@GetMapping("/admin") @PreAuhorize
											  
  PACOTE -> execption  	= CLASS TokenRefreshException.java extends RuntimeException	->@ResponseStatus	
  
  PACOTE -> models		= ENUM	EnunPessoa.java
						= CLASS	RefreshToken.java 
						= CLASS Pessoa.java
						= CLASS Usuario.java
  
  PACOTE -> payload
  
  PACOTE -> repository
  
  PACOTE -> security