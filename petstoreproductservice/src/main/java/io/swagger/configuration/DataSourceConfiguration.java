package io.swagger.configuration;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DataSourceConfiguration {

  public static final Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);

  @Value("${petstore.vault.managed-identity-client-id}")
  private String managedIdentityClientId;

  @Value("${petstore.vault.url-name}")
  private String url;

  @Value("${petstore.vault.username-name}")
  private String username;

  @Value("${petstore.vault.password-name}")
  private String password;

  @Value("${petstore.vault.vault-name}")
  private String keyVaultName;

  private String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

  private final DataSourceProperties dataSourceProperties;

  @Autowired
  public DataSourceConfiguration(DataSourceProperties dataSourceProperties) {
    this.dataSourceProperties = dataSourceProperties;
  }

  @Bean
  @ConditionalOnProperty(name = "database.enabled", havingValue = "true", matchIfMissing = true)
  public DataSource dataSource() {

    Map<String, String> vaultCredentials = getValuesFromKeyVault();
    if(!vaultCredentials.isEmpty()) {
      return DataSourceBuilder.create()
          .url(vaultCredentials.get(url))
          .username(vaultCredentials.get(username))
          .password(vaultCredentials.get(password))
          //.driverClassName(dataSourceProperties.getDriverClassName())
          .build();
    }
    log.error("Unable to get credentials for database");
    throw new RuntimeException("Unable to get credentials for database");
  }

  public Map<String, String> getValuesFromKeyVault() {
    try {
      DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder()
          .managedIdentityClientId(managedIdentityClientId)
          .build();

      SecretClient secretClient = new SecretClientBuilder()
          .vaultUrl(keyVaultUri)
          .credential(defaultCredential)
          .buildClient();

      KeyVaultSecret retrievedSecretUrl = secretClient.getSecret(url);
      KeyVaultSecret retrievedSecretUsername = secretClient.getSecret(username);
      KeyVaultSecret retrievedSecretPassword = secretClient.getSecret(password);
      log.info("Getting credentials from Key Vault using Managed Identity");
      return Map.of(url, retrievedSecretUrl.getValue(), username, retrievedSecretUsername.getValue(),
          password, retrievedSecretPassword.getValue());
    } catch (Exception e) {
      log.warn("Key Vault was not able to provide credentials, using from properties");
      return Map.of(url, dataSourceProperties.getUrl(), username, dataSourceProperties.getUsername(),
          password, dataSourceProperties.getPassword());
    }
  }
}
