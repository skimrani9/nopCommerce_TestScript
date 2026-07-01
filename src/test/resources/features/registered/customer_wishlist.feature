# @Registered @customer @smoke @regression @registered-wishlist

# Feature: Customer Wishlist

#   As a registered customer
#   I want to save products to my wishlist
#   So that I can review and purchase them later

#   @TC-NC-TR01007 @RegisteredWishlistFlow @requires-data
#   Scenario: Add product to wishlist from detail page
#     Given a registered E2E customer account exists
#     And customer is logged into the storefront
#     When registered customer opens the E2E guest product detail page
#     And registered customer adds the product to wishlist
#     Then wishlist confirmation is shown

#   @TC-NC-TR01008 @RegisteredWishlistFlow @requires-data
#   Scenario: View wishlist page with saved item
#     Given a registered E2E customer account exists
#     And registered customer has E2E guest product in wishlist
#     And customer is logged into the storefront
#     When registered customer opens the wishlist page
#     Then wishlist displays the E2E guest product with price

#   @TC-NC-TR01009 @RegisteredWishlistFlow @requires-data
#   Scenario: Move wishlist item to cart
#     Given a registered E2E customer account exists
#     And registered customer has E2E guest product in wishlist
#     And customer is logged into the storefront
#     When registered customer moves wishlist item to cart
#     Then shopping cart contains the E2E guest product
