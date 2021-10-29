package com.finerio.api.helper;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.finerio.api.exception.ReadValueException;
import com.finerio.api.exception.UnknownResourceException;
import com.finerio.api.model.ErrorException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestTemplateHelper {

	private RestTemplate restTemplate;
	private HttpHeaders headers = new HttpHeaders();
    private ObjectMapper objectMapper;

	public RestTemplateHelper() {
		log.debug(" --- Constructor Initializer  --- ");

		this.restTemplate = new RestTemplate();
		ClientHttpRequestFactory factory = this.restTemplate.getRequestFactory();

		if (factory instanceof SimpleClientHttpRequestFactory) {
			((SimpleClientHttpRequestFactory) factory).setConnectTimeout(60 * 4 * 1000);
			((SimpleClientHttpRequestFactory) factory).setReadTimeout(60 * 4 * 1000);
		} else if (factory instanceof HttpComponentsClientHttpRequestFactory) {
			((HttpComponentsClientHttpRequestFactory) factory).setReadTimeout(60 * 4 * 1000);
			((HttpComponentsClientHttpRequestFactory) factory).setConnectTimeout(60 * 4 * 1000);

		}
		this.restTemplate.setRequestFactory(factory);
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		this.objectMapper = new ObjectMapper();
	}

    public <T> T getForEntity(Class<T> clazz, String url, Object... uriVariables) {
    	log.debug(" --- getForEntity  --- ");
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
            return readValue(response, javaType);
        } catch (HttpClientErrorException exception) {
			if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.error("No data found {} , getForList : " + url);
				throw new UnknownResourceException("No se encontraron datos :" + url);

			} else {
				log.error("rest client exception getForEntity  " + exception.getMessage());

				throw new RestClientException( exception.getMessage());
			}
		} catch (RestClientResponseException rre) {
			log.error(
					"RestClientResponseException  getForEntity : " + rre.getResponseBodyAsString());
			log.error("RestClientResponseException  getForEntity : ", rre);
			throw new RestClientException(rre.getResponseBodyAsString());
		}
    }

	public <T> List<T> getForList(Class<T> clazz, String url, Object... uriVariables) {
		log.debug(" --- getForList  --- ");
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
			CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
			return readValue(response, collectionType);
		} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.error("No data found {} , getForList :" + url);
				throw new UnknownResourceException("No se encontraron datos :" + url);

			} else {
				log.error("rest client exception getForList :" + exception.getMessage());

				throw new RestClientException( exception.getMessage());
			}
		} catch (RestClientResponseException rre) {
			log.error(
					"RestClientResponseException  getForList :" + rre.getResponseBodyAsString());
			log.error("RestClientResponseException  getForList :", rre);
			throw new RestClientException(rre.getResponseBodyAsString());
		}
	}
	
	  public <T, R> List<T> postForList(Class<T> clazz, String url, R body, Object... uriVariables) {
	        log.debug(" --- postForList  --- ");
	        try {
	        HttpEntity<R> request = new HttpEntity<>(body);
	        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, uriVariables);
	           CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
	        return readValue(response, collectionType);
	        } catch (HttpClientErrorException exception) {
	            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
	                log.error("No data found {} , postForList  " + url);
	                throw new UnknownResourceException("No se encontraron datos :" + url);

	            } else {
	                log.error("rest client exception postForList : " + exception.getMessage());

	                throw new RestClientException(exception.getMessage());
	            }
	        } catch (RestClientResponseException rre) {
	            log.error("RestClientResponseException : " + rre.getResponseBodyAsString());
	            log.error("RestClientResponseException  : ", rre);
	            throw new RestClientException(rre.getResponseBodyAsString());
	        }catch (Throwable exceptionThrowable) {
	            log.error(" Throwable Error: " + exceptionThrowable.getMessage());
	            log.error(" Throwable Error :", exceptionThrowable);
	        }
	        return null;
	    }

    public <T, R> T postForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
    	log.debug(" --- postForEntity  --- ");
    	try {
        HttpEntity<R> request = new HttpEntity<>(body);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, uriVariables);
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        return readValue(response, javaType);
    	} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.error("No data found {} , postForEntity  " + url);
				throw new UnknownResourceException("No se encontraron datos :" + url);

			} else {
				log.error("rest client exception postForEntity : " + exception.getMessage());

				throw new RestClientException(exception.getMessage());
			}
		} catch (RestClientResponseException rre) {
			log.error("RestClientResponseException : " + rre.getResponseBodyAsString());
			log.error("RestClientResponseException  : ", rre);
			throw new RestClientException(rre.getResponseBodyAsString());
		}catch (Throwable exceptionThrowable) {
		    log.error(" Throwable Error: " + exceptionThrowable.getMessage());
            log.error(" Throwable Error :", exceptionThrowable);
		}
        return null;
    }

	public <T, R> T putForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
		log.debug(" --- putForEntity --- ");
		try {
			HttpEntity<R> request = new HttpEntity<>(body);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class,
					uriVariables);
			JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
			return readValue(response, javaType);

		} catch (HttpClientErrorException exception) {
			if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.error("No data found {} , putForEntity : " + url);
				throw new UnknownResourceException("No se encontraron datos :" + url);

			} else {
				log.error("rest client exception putForEntity : " + exception.getMessage());

				throw new RestClientException(exception.getMessage());
			}
		} catch (RestClientResponseException rre) {
			log.error(
					" " + rre.getResponseBodyAsString());
			log.error("RestClientResponseException  putForEntity: ", rre);
			throw new RestClientException(rre.getResponseBodyAsString());
		}
	}

    public void delete(String url, Object... uriVariables) {
    	log.debug(" --- delete  --- ");
        try {
            restTemplate.delete(url, uriVariables);
        } catch (RestClientException exception) {
            log.error(exception.getMessage());
            throw new RestClientException(exception.getMessage());

        }
        
    }

	public <T> T readValue(ResponseEntity<String> response, JavaType javaType) {
		log.debug(" --- readValue --- ");
		T result = null;
		if (response.getStatusCode() == HttpStatus.OK
				|| response.getStatusCode() == HttpStatus.CREATED) {
			try {
				result = objectMapper.readValue(response.getBody(), javaType);
			} catch (IOException e) {
				log.error(e.getMessage());				
				try {
					javaType = objectMapper.getTypeFactory().constructType(ErrorException.class);
					
					result = objectMapper.readValue(response.getBody(),javaType);
					
					ErrorException exception = (ErrorException) result; 
					
					if(exception.getExcepcion() != null && !exception.getExcepcion().equals("")){
						throw new ReadValueException(exception.getExcepcion()); 
					}else if(exception.getException() != null && !exception.getException().equals("")){
						throw new ReadValueException(exception.getException()); 
					}
					
				} catch (Exception ex) {
					log.error(ex.getMessage());
					throw new ReadValueException(ex.getMessage()); 
				}catch (Throwable throwable) {
					throw new RestClientException(throwable.getMessage());
				}
			}
		} else {
			log.error("No data found {}" + response.getStatusCode());
			throw new UnknownResourceException("No se encontro informacion :" + response.getStatusCode());
		}
		return result;
	}
}
