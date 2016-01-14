package info.blockchain.wallet;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import info.blockchain.wallet.multiaddr.MultiAddrFactory;
import info.blockchain.wallet.payload.Account;
import info.blockchain.wallet.payload.ImportedAccount;
import info.blockchain.wallet.payload.LegacyAddress;
import info.blockchain.wallet.payload.PayloadBridge;
import info.blockchain.wallet.payload.PayloadFactory;
import info.blockchain.wallet.util.ToastCustom;

import java.util.ArrayList;
import java.util.List;

import piuk.blockchain.android.R;

public class AccountEditActivity extends AppCompatActivity {

    private TextView tvLabel = null;
    private EditText etLabel = null;
    private TextView tvSave = null;
    private TextView tvCancel = null;

    private Account account = null;
    private LegacyAddress legacyAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initToolbar();
        getIntentData();
        setupViews();
    }

    private void setupViews() {

        tvLabel = (TextView) findViewById(R.id.tv_label);
        if(account != null){
            tvLabel.setText(getString(R.string.name));//V3
        }else{
            tvLabel.setText(getString(R.string.label));//V2
        }

        etLabel = (EditText) findViewById(R.id.account_name);

        if (account != null)
            etLabel.setText(account.getLabel());
        else if (legacyAddress != null)
            etLabel.setText(legacyAddress.getLabel());

        tvSave = (TextView) findViewById(R.id.confirm_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        tvCancel = (TextView) findViewById(R.id.confirm_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private void getIntentData() {

        int accountIndex = getIntent().getIntExtra("account_index", -1);
        int addressIndex = getIntent().getIntExtra("address_index", -1);

        if (accountIndex >= 0) {

            //V3
            List<Account> accounts = PayloadFactory.getInstance().get().getHdWallet().getAccounts();

            //Remove "All"
            List<Account> accountClone = new ArrayList<Account>(accounts.size());
            accountClone.addAll(accounts);

            if (accountClone.get(accountClone.size() - 1) instanceof ImportedAccount) {
                accountClone.remove(accountClone.size() - 1);
            }

            account = accountClone.get(accountIndex);

        } else if (addressIndex >= 0) {

            //V2
            ImportedAccount iAccount = null;
            if (PayloadFactory.getInstance().get().getLegacyAddresses().size() > 0) {
                iAccount = new ImportedAccount(getString(R.string.imported_addresses), PayloadFactory.getInstance().get().getLegacyAddresses(), new ArrayList<String>(), MultiAddrFactory.getInstance().getLegacyBalance());
            }
            if (iAccount != null) {

                List<LegacyAddress> legacy = iAccount.getLegacyAddresses();
                legacyAddress = legacy.get(addressIndex);
            }
        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar_general);
        toolbar.setTitle(getResources().getString(R.string.edit));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveChanges() {

        String newLabel = etLabel.getText().toString();

        if (newLabel.isEmpty()) {

            ToastCustom.makeText(this,getString(R.string.label_cant_be_empty),ToastCustom.LENGTH_LONG,ToastCustom.TYPE_ERROR);

        } else {
            if (legacyAddress != null) {
                legacyAddress.setLabel(newLabel);
            } else if (account != null) {
                account.setLabel(newLabel);
            }

            ToastCustom.makeText(this,getString(R.string.saving_changes),ToastCustom.LENGTH_LONG,ToastCustom.TYPE_OK);
            PayloadBridge.getInstance(this).remoteSaveThread();
            setResult(RESULT_OK);
            finish();
        }
    }
}
