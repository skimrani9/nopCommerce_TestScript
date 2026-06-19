@Administrator @admin-shipments @admin @smoke @regression @Sanity @StableTests

Feature: Shipment Management

  As an administrator
  I want to manage order shipments
  So that customers receive tracking and delivery updates

  @TC-NC-T35001 @AdminShipmentFlow @requires-data
  Scenario: View shipment list
    Given admin is logged into the admin panel
    And at least one shipment exists in the system
    When admin navigates to the shipment list
    Then shipment grid is displayed with shipment records

  @TC-NC-T35002 @AdminShipmentFlow @requires-data
  Scenario: Create shipment for order
    Given admin is logged into the admin panel
    And a processing paid order exists for automation
    When admin creates a shipment for the order
    Then shipment is created and linked to the order

  @TC-NC-T35003 @AdminShipmentFlow @requires-data
  Scenario: Set shipment tracking number
    Given admin is logged into the admin panel
    And a shipment exists for automation
    When admin sets shipment tracking number to "1Z999AA10123456784"
    Then shipment tracking number is saved as "1Z999AA10123456784"

  @TC-NC-T35004 @AdminShipmentFlow @requires-data
  Scenario: Mark shipment as shipped
    Given admin is logged into the admin panel
    And a shipment exists for automation
    When admin marks the shipment as shipped
    Then shipment status reflects shipped state

  @TC-NC-T35005 @AdminShipmentFlow @requires-data
  Scenario: Mark shipment as delivered
    Given admin is logged into the admin panel
    And a shipped shipment exists for automation
    When admin marks the shipment as delivered
    Then shipment status reflects delivered state

  @TC-NC-T35006 @AdminShipmentFlow @requires-data
  Scenario: Download packaging slip PDF
    Given admin is logged into the admin panel
    And a shipment exists for automation
    When admin views shipment details
    Then PDF packaging slip link is available
