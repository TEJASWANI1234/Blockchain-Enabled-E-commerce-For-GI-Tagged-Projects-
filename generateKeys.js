const ethers = require('ethers');

// Generate a new random private key
const privateKey = ethers.Wallet.createRandom().privateKey;
console.log('Private Key:', privateKey);

// Derive the public key from the private key
const wallet = new ethers.Wallet(privateKey);
const publicKey = wallet.address;
console.log('Public Key:', publicKey);
