<h1>Implement a REST API for a Todo app</h1>

<h2>User Authentication</h2> 
• Register: Allow users to create new accounts using their email and password. 
<br>
• Login: Enable users to authenticate with their email and password. 
<br>
<h2>Todo Management </h2>
• Create: Allow users to create new Todo items. <br>
• Read: Enable users to retrieve their own Todo items. <br>
• Update: Permit users to modify existing Todo items. 
• Delete: Allow users to remove Todo items. 
<br>
<h2>Authorization</h2>
• Private Data: Ensure that users can only access and modify their own Todo items. <br>
• Security: Implement appropriate security measures to protect user data. 


<h2>Testing</h2>

• AuthControllerTests.java: Tests related to user authentication (register and login).<br>
• TodoControllerTests.java: Tests for Todo CRUD operations (create, read, update, delete).

<h2>Additional Consideration</h2>

• Error Handling: Use @ControllerAdvice for consistent error responses.<br>
• Pagination and Sorting: Managed by Spring Data JPA's Pageable.<br>
• Search: Add keyword parameter in getTodos.<br>
• Logging: Use @Slf4j or Logger for error and request tracking.<br>
• sorting : Enable sorting with Pageable, adding sort parameters to requests.<br>
• Completion Status: Track Todo status with a completed field and expose it via an endpoint.

