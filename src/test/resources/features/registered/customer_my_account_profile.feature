# @Registered @customer @smoke @regression @registered-profile

# Feature: My Account Profile

#   As a registered customer
#   I want to view and update my profile information
#   So that my account details stay current

#   @TC-NC-TR01004 @RegisteredProfileFlow @requires-data
#   Scenario: View customer info page
#     Given a registered E2E customer account exists
#     And customer is logged into the storefront
#     When registered customer opens the customer info page
#     Then customer info page displays profile fields with current values

#   @TC-NC-TR01005 @RegisteredProfileFlow @requires-data
#   Scenario: Update profile first and last name
#     Given a registered E2E customer account exists
#     And customer is logged into the storefront
#     When registered customer opens the customer info page
#     And registered customer updates first name to "Jonathan" and last name to "Doe-Smith"
#     Then profile update success is displayed
#     And customer info shows first name "Jonathan" and last name "Doe-Smith"

#   @TC-NC-TR01006 @RegisteredProfileFlow
#   Scenario: Customer info page requires authentication
#     When guest navigates to the customer info page directly
#     Then guest is redirected to the login page
