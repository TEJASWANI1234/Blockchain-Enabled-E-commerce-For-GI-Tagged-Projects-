import qrcode

# Replace with your actual website URL and product ID
url = 'https://yourwebsite.com/product?productId=12345'

# Generate QR code
qr = qrcode.make(url)

# Save the QR code
qr.save('product_qr_code.png')
