# Java Servlet, JDBC, and MySQL QA Guide

This is a Java web application using Servlets, JDBC, MySQL, and Tomcat 9, organized as an Eclipse Dynamic Web Project. Check the current deployment descriptor, servlet mappings, SQL schema, and Java source for exact endpoints and required versions.

## Prerequisites and setup

- JDK compatible with the project and Tomcat 9
- Apache Tomcat 9
- MySQL Server
- MySQL JDBC driver
- Eclipse or another compatible Java web project workflow

Use the schema and setup instructions checked into the repository. Configure database credentials outside source control; never commit passwords or real student records.

## Functional test matrix

For each implemented CRUD workflow, test:
- Valid create/read/update/delete operations
- Missing fields, malformed values, and invalid identifiers
- Duplicate student records and unknown record IDs
- Database unavailable, invalid credentials, and failed writes
- Session expiry and sign-out behavior
- Unauthorized attempts to access or edit another user's record

## JDBC and security requirements

- Use prepared statements for user-controlled values.
- Validate input on the server, not only in browser forms.
- Close JDBC resources reliably and use transactions for multi-step writes.
- Protect session cookies and enforce authorization for every protected servlet.
- Return safe error pages/messages; do not expose stack traces, SQL details, or credentials.
- Avoid logging student names and academic data unnecessarily.

## Validation and deployment

Test locally using a dedicated database with synthetic records. Verify the deployed artifact in Tomcat and check server logs for startup errors. Do not claim a successful deployment solely because the project compiles in the IDE.

## Pull-request checklist

- [ ] Test positive and negative CRUD paths.
- [ ] Verify authorization and session behavior.
- [ ] Run compile/build checks and any available tests.
- [ ] Confirm database credentials and student data are not committed.
- [ ] Document the exact Tomcat/JDK/MySQL setup and any unrun checks.
