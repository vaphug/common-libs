# common-webclient

## Tom tat
Wrapper WebClient cho gọi HTTP chuẩn hóa headers/context/request model.

## Public services/functions
Danh sach duoi day duoc trich truc tiep tu source de bao phu toan bo API public cua module.

```text
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:11:public class CommonWebClient {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:15:    public CommonWebClient(WebClient webClient) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:19:    public <T> Mono<T> get(String uri, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:23:    public <T> Mono<T> get(String uri, Map<String, String> queryParams, Consumer<HttpHeaders> headersCustomizer,
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:36:    public <T> Mono<List<T>> getList(String uri, Class<T> itemType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:40:    public <T> Mono<List<T>> getList(String uri, Map<String, String> queryParams, Consumer<HttpHeaders> headersCustomizer,
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:54:    public <T, B> Mono<T> post(String uri, B body, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:58:    public <T, B> Mono<T> post(String uri, B body, Consumer<HttpHeaders> headersCustomizer, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:67:    public <T, B> Mono<T> put(String uri, B body, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:71:    public <T, B> Mono<T> put(String uri, B body, Consumer<HttpHeaders> headersCustomizer, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:80:    public <T> Mono<T> delete(String uri, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:84:    public <T> Mono<T> delete(String uri, Consumer<HttpHeaders> headersCustomizer, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/CommonWebClient.java:92:    public Mono<Void> delete(String uri) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:12:public class WebClientBuilder {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:16:    public WebClientBuilder(WebClient.Builder delegate) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:20:    public WebClientBuilder baseUrl(String baseUrl) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:27:    public WebClientBuilder defaultHeader(String name, String value) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:34:    public WebClientBuilder defaultHeaders(Map<String, String> headers) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:42:    public WebClientBuilder headers(Consumer<HttpHeaders> headersConsumer) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:49:    public WebClientBuilder maxInMemorySize(int maxInMemorySizeBytes) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:57:    public WebClientBuilder responseTimeout(Duration timeout) {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:66:    public WebClient build() {
common-webclient/src/main/java/com/yourdomain/common/webclient/client/WebClientBuilder.java:70:    public CommonWebClient buildClient() {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:12:public class WebClientService {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:17:    public WebClientService(CommonWebClient commonWebClient, UserContextHelper userContextHelper) {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:22:    public <T> Mono<T> get(WebClientRequest request, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:27:    public <T> Mono<List<T>> getList(WebClientRequest request, Class<T> itemType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:32:    public <T> Mono<T> post(WebClientRequest request, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:37:    public <T> Mono<T> put(WebClientRequest request, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:42:    public <T> Mono<T> delete(WebClientRequest request, Class<T> responseType) {
common-webclient/src/main/java/com/yourdomain/common/webclient/service/WebClientService.java:47:    public Map<String, String> inheritedHeaders(Map<String, String> incomingHeaders, boolean batchMode) {
common-webclient/src/main/java/com/yourdomain/common/webclient/model/WebClientRequest.java:13:public class WebClientRequest {
common-webclient/src/main/java/com/yourdomain/common/webclient/model/WebClientRequest.java:24:    public static WebClientRequest of(String uri) {
common-webclient/src/main/java/com/yourdomain/common/webclient/context/UserContextHelper.java:10:public class UserContextHelper {
common-webclient/src/main/java/com/yourdomain/common/webclient/context/UserContextHelper.java:22:    public Map<String, String> resolveInheritedHeaders(Map<String, String> incomingHeaders, boolean batchMode) {
common-webclient/src/main/java/com/yourdomain/common/webclient/context/UserContextHelper.java:43:    public Map<String, String> resolveInheritedHeaders(HttpHeaders incomingHeaders, boolean batchMode) {
common-webclient/src/main/java/com/yourdomain/common/webclient/config/WebClientProperties.java:15:public class WebClientProperties {
common-webclient/src/main/java/com/yourdomain/common/webclient/config/WebClientAutoConfiguration.java:19:public class WebClientAutoConfiguration {
common-webclient/src/main/java/com/yourdomain/common/webclient/config/WebClientAutoConfiguration.java:29:    public WebClient.Builder webClientBuilder(WebClientProperties properties) {
```

## 3rd-party API / thu vien lien quan
- Spring WebFlux WebClient: https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html
- Reactor Netty: https://projectreactor.io/docs/netty/release/reference/

## Module lien quan
- [common-notification](../common-notification)
