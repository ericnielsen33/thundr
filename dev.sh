export MODE=DEV
export CUSTOMER_PREFIX=p1pmx_prospect
export DAC_CLIENT_NAME=p1pmx_prospect
export BASE_PATH=s3://pmx-prod-uc-us-east-1-databricks-iq
export PROD_PATH=${BASE_PATH}/thundr/prod/
export QA_PATH=${BASE_PATH}/thundr/qa/
export DAC_API_KEY=na

echo ${DAC_CLIENT_NAME}