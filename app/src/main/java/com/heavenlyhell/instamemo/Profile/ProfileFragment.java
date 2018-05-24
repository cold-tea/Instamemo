package com.heavenlyhell.instamemo.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heavenlyhell.instamemo.R;
import com.heavenlyhell.instamemo.models.Constants;
import com.heavenlyhell.instamemo.models.User;
import com.heavenlyhell.instamemo.models.UserAccountSetting;
import com.heavenlyhell.instamemo.models.UserSetting;
import com.heavenlyhell.instamemo.utils.BottomNavigationViewHelper;
import com.heavenlyhell.instamemo.utils.GridImageAdapter;
import com.heavenlyhell.instamemo.utils.ItemOffsetDecoration;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sande on 3/7/2018.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ImageView ivProfileAppbar;
    private ProgressBar pbProfile;
    private RecyclerView rvProfile;
    private TextView tvProfileBtnEdit;
    private TextView tvProfilePosts, tvProfileFollowers, tvProfileFollowing;
    private TextView tvProfileDisplayName, tvProfileStatus, tvProfileWebsite;
    private TextView tvProfileAppbar;
    private CircleImageView ivProfileMain;
    private DatabaseReference dbReference;
    private Toolbar tbProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbReference = FirebaseDatabase.getInstance().getReference();
        ivProfileAppbar = view.findViewById(R.id.ivProfileAppbar);
        pbProfile = view.findViewById(R.id.pbProfile);
        rvProfile = view.findViewById(R.id.rvProfile);
        tbProfile = view.findViewById(R.id.tbProfile);
        tvProfileBtnEdit = view.findViewById(R.id.tvProfileButtonEdit);

        tvProfilePosts = view.findViewById(R.id.tvProfilePosts);
        tvProfileFollowers = view.findViewById(R.id.tvProfileFollwers);
        tvProfileFollowing = view.findViewById(R.id.tvProfileFollowing);
        tvProfileDisplayName = view.findViewById(R.id.tvProfileDisplayName);
        tvProfileStatus = view.findViewById(R.id.tvProfileStatus);
        tvProfileWebsite = view.findViewById(R.id.tvProfileWebsite);
        ivProfileMain = view.findViewById(R.id.ivProfileMain);
        tvProfileAppbar = view.findViewById(R.id.tvProfileAppbar);

        ivProfileAppbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(getContext(),
                        AccountsettingActivity.class);
                startActivity(intentSettings);
            }
        });

        tvProfileBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.flProfileContainer, new EditProfileFragment(),
                        getString(R.string.fragment_edit_profile));
                transaction.addToBackStack(getString(R.string.fragment_edit_profile));
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pbProfile.setVisibility(View.GONE);
        setUpBottomNavigation();
        tempRecyclerSetup();
        updateFields();
    }

    private void updateFields() {
        pbProfile.setVisibility(View.VISIBLE);
        dbReference.child(getString(R.string.dbname_user_account_settings)).child(FirebaseAuth.getInstance
                ().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccountSetting userAccount = dataSnapshot.getValue(UserAccountSetting.class);
                configureUserSettingObject(userAccount);

                tvProfilePosts.setText(userAccount.getPosts());
                tvProfileFollowers.setText(userAccount.getFollowers());
                tvProfileFollowing.setText(userAccount.getFollowing());
                tvProfileDisplayName.setText(userAccount.getDisplay_name());
                tvProfileWebsite.setText(userAccount.getWebsite());
                tvProfileStatus.setText(userAccount.getDescription());
                tvProfileAppbar.setText(userAccount.getUsername());
                if (!TextUtils.isEmpty(userAccount.getProfile_photo()))
                    Picasso.with(getContext()).load(userAccount.getProfile_photo())
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(ivProfileMain);
                pbProfile.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), getString(R.string.profile_load_problem),
                        Toast.LENGTH_LONG).show();
                pbProfile.setVisibility(View.GONE);
            }
        });
        dbReference.child(getString(R.string.dbname_user_account_settings) +
            "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).keepSynced(true);
    }

    private void configureUserSettingObject(UserAccountSetting userAccount) {
        Constants.userSetting.setUserAccount(userAccount);
        dbReference.child(getString(R.string.dbname_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Constants.userSetting.setUser(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        dbReference.child(getString(R.string.dbname_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).keepSynced(true);
    }

    private void tempRecyclerSetup() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL,
                false);
        rvProfile.setHasFixedSize(true);
        rvProfile.setLayoutManager(manager);
        rvProfile.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset));
        //rvProfile.setNestedScrollingEnabled(false);
        rvProfile.setAdapter(new GridImageAdapter(getContext(), Arrays.asList(getResources().getStringArray(R
                .array.image_urls))));
    }

    private void setUpBottomNavigation() {
        BottomNavigationViewHelper.configureBottomNavigation(getActivity());
        MenuItem item = BottomNavigationViewHelper.bottomNavigation.getMenu().getItem(Constants.PROFILE_INDEX);
        item.setChecked(true);
    }
}
