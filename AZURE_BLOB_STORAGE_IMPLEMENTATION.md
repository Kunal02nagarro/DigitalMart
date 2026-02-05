# Azure Blob Storage Integration - Implementation Summary

## Overview
Added complete Azure Blob Storage integration to the DigitalMart application to manage product images. Products can now store image names, which are resolved to full Azure Blob Storage URLs for display.

## Changes Made

### 1. **Dependencies** (`pom.xml`)
- Added `azure-storage-blob` dependency (v12.24.0) for Azure Blob Storage SDK

### 2. **Database Model** (`Product.java`)
- Added `imageName` field to store the name of the image file in Azure Blob Storage
- Added getter and setter methods for `imageName`

### 3. **Database Migration** (`create-tables-for-digital-mart.xml`)
- Added `image_name` VARCHAR(255) column to the `product` table
- Column is nullable to allow existing products without images

### 4. **Configuration** (`application.properties`)
Added new Azure Blob Storage configuration properties:
```properties
# Azure Blob Storage Configuration
azure.storage.account.name=${AZURE_STORAGE_ACCOUNT_NAME}
azure.storage.account.key=${AZURE_STORAGE_ACCOUNT_KEY}
azure.storage.container.name=${AZURE_STORAGE_CONTAINER_NAME}
```

### 5. **New Service** (`BlobStorageService.java`)
Created a new service class to handle all Azure Blob Storage operations:
- `getBlobImageUrl(String imageName)` - Converts image name to full blob URL
- `uploadBlob(String blobName, byte[] data)` - Uploads image to blob storage
- `deleteBlob(String blobName)` - Deletes image from blob storage

### 6. **Product Service** (`ProductService.java`)
- Added `BlobStorageService` autowiring
- Added `getProductImageUrl(Long productId)` - Gets image URL for a product
- Added `getBlobImageUrl(String imageName)` - Gets URL for an image name

### 7. **Templates Updates**

#### `add-product.html`
- Added "Image Name" input field
- Users can enter the Azure Blob Storage image filename when adding products

#### `products.html`
- Added product image display in the product card
- Shows image if available, displays placeholder (📷) if no image
- Updated CSS for image styling (200px height, full width, object-fit cover)

#### `product-detail.html`
- Added large product image display at the top
- Shows image if available, displays placeholder (📷) if no image
- Updated CSS for detail page image styling (400px height)

## How to Use

### Environment Setup
Set the following environment variables:
```bash
AZURE_STORAGE_ACCOUNT_NAME=your-storage-account-name
AZURE_STORAGE_ACCOUNT_KEY=your-storage-account-key
AZURE_STORAGE_CONTAINER_NAME=your-container-name
```

### Adding Products with Images
1. Upload your image to Azure Blob Storage container
2. Note the image filename (e.g., `product-image.jpg`)
3. Go to "Add Product" page
4. Fill in product details and enter the image name in the "Image Name" field
5. Submit the form

### How It Works
- When viewing products, the image name is fetched from the database
- The `ProductService` constructs the full Azure Blob Storage URL using:
  - Storage account name
  - Container name
  - Image filename
- The URL is used in the `<img>` tag to display the image
- If no image name is provided, a placeholder emoji (📷) is shown

## API Integration
The `BlobStorageService` provides a clean API for image operations:

```java
// Get full URL for an image
String url = blobStorageService.getBlobImageUrl("product-image.jpg");
// Returns: https://youraccountname.blob.core.windows.net/yourcontainer/product-image.jpg

// Upload a new image
byte[] imageData = Files.readAllBytes(Paths.get("image.jpg"));
blobStorageService.uploadBlob("product-image.jpg", imageData);

// Delete an image
blobStorageService.deleteBlob("product-image.jpg");
```

## Template Usage
In Thymeleaf templates, use the `@productService` bean:
```html
<img th:src="${@productService.getBlobImageUrl(product.imageName)}" 
     alt="Product Image" 
     class="product-image">
```

## Error Handling
- Missing image names are handled gracefully (no URL generated)
- Failed image loads display placeholder (📷)
- Invalid blob URLs fail silently without breaking the page

## Database Considerations
- The `image_name` column is nullable for backward compatibility
- No data migration needed for existing products (they just won't have images)
- New products can have images assigned via the "Add Product" form

## Next Steps (Optional Enhancements)
1. Add image upload functionality to upload files directly to Azure
2. Add image editing/deletion for existing products
3. Implement image resizing/thumbnails
4. Add image validation (file type, size limits)
5. Cache blob URLs for better performance
