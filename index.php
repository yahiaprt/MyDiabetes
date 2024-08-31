<?php
require 'vendor/autoload.php';
if(isset($_POST['authKey']) && ($_POST['authKey'] == "abc")) {
$stripe = new \Stripe\StripeClient('sk_test_51NLQheAT1ylZuIeCNf1WBXKBqI9tg00xsgwfCpqMSGH80Bc9D5Q8QAFxq60HHYdEEFH99Vw1iDRqNEVAHpicq7Zx00C05taCef');

// Use an existing Customer ID if this is a returning customer.
$customer = $stripe->customers->create(
[
'name' => 'Jenny Rosen',
'address' => [
'line1' => '510 Townsend St',
'postal_code' => '98140',
'city' => 'San Francisco',
'state' => 'CA',
'country' => 'US',
]
]
);
$ephemeralKey = $stripe->ephemeralKeys->create([
  'customer' => $customer->id,
], [
  'stripe_version' => '2022-08-01',
]);
$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 1099,
  'currency' => 'eur',
  'description' => 'yaya prt',
  'customer' => $customer->id,
  'automatic_payment_methods' => [
    'enabled' => 'true',
  ],
]);

echo json_encode(
  [
    'paymentIntent' => $paymentIntent->client_secret,
    'ephemeralKey' => $ephemeralKey->secret,
    'customer' => $customer->id,
    'publishableKey' => 'pk_test_51NLQheAT1ylZuIeCWkZyuNgBu5LH9HpNcOSsqWkimTzHZkMylI0wgUCSLKrBiGmEBHEKhX2t7NzM3NmaAuDe3YKZ00v5EMPYxE'
  ]
);
http_response_code(200);
}
else {
  http_response_code(403);
}
