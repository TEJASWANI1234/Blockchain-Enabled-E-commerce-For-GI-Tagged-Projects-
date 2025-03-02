const QRCode = require('qrcode');
const publicKey = '0xb2da5E1AA02407F30559f4264966a2bAEff81373';
const Jimp = require('jimp');
const readline = require('readline');

async function generateQRCode() {
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    const productId = await new Promise((resolve) => {
        rl.question('Enter the product ID: ', (answer) => {
            resolve(answer);
        });
    });

    const productName = await new Promise((resolve) => {
        rl.question('Enter the product name: ', (answer) => {
            resolve(answer);
        });
    });

    const productPrice = await new Promise((resolve) => {
        rl.question('Enter the product price: ', (answer) => {
            resolve(answer);
        });
    });

    const productSpecifications = await new Promise((resolve) => {
        rl.question('Enter the product specifications: ', (answer) => {
            resolve(answer);
        });
    });
    
    rl.close();

    const qrData = {
        id: productId,
        name: productName,
        price: productPrice,
        specifications: productSpecifications,
    };

    const qrCodeString = JSON.stringify(qrData);

    QRCode.toFile('qr_code.png', qrCodeString, {
        color: {
            dark: '#000000',
            light: '#ffffff',
        },
    },async (err) => {
        if (err) {
            console.error('Error generating QR code:', err);
            return;
        }
    
        // Open the generated QR code image using Jimp
        const qrCodeImage = await Jimp.read('qr_code.png');
    
        // Open the verified tick mark image using Jimp
        const tickImage = await Jimp.read('verified_tick.png'); // Replace with the path to the verified tick mark image file
    
        // Resize the tick mark image to fit within the QR code
        tickImage.resize(qrCodeImage.bitmap.width * 0.2, qrCodeImage.bitmap.width * 0.2);
    
        // Calculate the position for the tick mark overlay
        const x = qrCodeImage.bitmap.width - tickImage.bitmap.width - 10;
        const y = qrCodeImage.bitmap.height - tickImage.bitmap.height - 10;
    
        // Compose the QR code image and the tick mark overlay
        qrCodeImage.composite(tickImage, x, y);
    
        // Save the final QR code image with the tick mark overlay
        qrCodeImage.write('qr_code_with_tick.png');
    
});
console.log('QR code generated successfully!');
}

generateQRCode();
